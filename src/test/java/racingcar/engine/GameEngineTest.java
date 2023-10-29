package racingcar.engine;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import racingcar.domain.Score;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

class GameEngineTest {
    private static final int MAX_SIZE = 5;

    private static String[] 점수_생성이_안되는_케이스() {
        return new String[]{" ".repeat(MAX_SIZE + 1), "123456789"};
    }

    private static String[] 점수_생성이_되는_케이스() {
        return new String[]{" ".repeat(MAX_SIZE), "12345"};
    }

    @Test
    void 플레이어이름이_null이면_예외를_던진다() {
        Assertions.assertThatCode(() -> new GameEngine(null, new ScoreUpdater(new ReturnGenerator()), new GameEngineValidator()))
                .isExactlyInstanceOf(IllegalArgumentException.class)
        ;
    }

    @ParameterizedTest
    @MethodSource("점수_생성이_안되는_케이스")
    void 플레이어이름이_6이상이면_예외를_던진다(String readLine) {
        Assertions.assertThatCode(() -> new GameEngine(readLine, new ScoreUpdater(new ReturnGenerator()), new GameEngineValidator()))
                .isExactlyInstanceOf(IllegalArgumentException.class)
        ;
    }

    @ParameterizedTest
    @MethodSource("점수_생성이_되는_케이스")
    void 플레이어이름이_5이하이면_예외를_던지지_않는다(String readLine) {
        Assertions.assertThatCode(() -> new GameEngine(readLine, new ScoreUpdater(new ReturnGenerator()), new GameEngineValidator()))
                .doesNotThrowAnyException();
    }

    @ParameterizedTest
    @CsvSource({
            "12",
            "1,2"
    })
    void 같은점수면_우승자순서대로_나오게한다(String playerNames) {
        List<Score> expectWinners = Arrays.stream(playerNames.split(",")).map(name -> new Score(name, 0L)).toList();
        List<Score> resultWinners = new GameEngine(playerNames, new ScoreUpdater(new ReturnGenerator()), new GameEngineValidator()).getWinners();
        Assertions.assertThat(resultWinners.stream().map(Score::getName).toList()).containsSequence(expectWinners.stream().map(Score::getName).toList());
        Assertions.assertThat(resultWinners.stream().map(Score::getScore).toList()).containsSequence(expectWinners.stream().map(Score::getScore).toList());
    }


}