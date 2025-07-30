import org.example.app.App;
import org.example.entity.Wise;
import org.example.repository.WiseRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Scanner;

import static org.assertj.core.api.Assertions.assertThat;

public class WiseTest {

    @Test
    @DisplayName("등록")
    void registerTest() {
        // given
        Scanner scanner = TestUtil.getScanner(
                """
                        등록
                        나의 죽음을 적들에게 알리지 말라!
                        이순신
                        종료
                        """
        );
        TestConfig config = new TestConfig(scanner);
        WiseRepository repository = config.wiseRepository();
        App app = initApp(config, repository, scanner);

        // when
        ByteArrayOutputStream bos = TestUtil.setOutToByteArray();
        app.run();
        TestUtil.clearSetOutToByteArray();

        // then
        String out = bos.toString().trim();
        assertThat(out)
                .contains("명언 :")
                .contains("작가 :")
                .contains("번 명언이 등록되었습니다.");

        List<Wise> wises = repository.findAll();
        assertThat(wises).hasSize(1);

        Wise wise = wises.getFirst();
        assertThat(wise.getId()).isEqualTo(1);
        assertThat(wise.getContent()).isEqualTo("나의 죽음을 적들에게 알리지 말라!");
        assertThat(wise.getAuthor()).isEqualTo("이순신");
    }

    @Test
    @DisplayName("목록")
    void listTest() {
        //given
        Scanner scanner = TestUtil.getScanner(
                """
                        등록
                        나의 죽음을 적들에게 알리지 말라!
                        이순신
                        등록
                        용기는 기도를 마친 두려움이다.
                        도로스 버나드
                        등록
                        돈이 수중에 들어오기 전까진 절대로 쓰지 마라.
                        토마스 제퍼슨
                        목록
                        종료
                        """
        );
        TestConfig config = new TestConfig(scanner);
        WiseRepository repository = config.wiseRepository();
        App app = initApp(config, repository, scanner);

        // when
        ByteArrayOutputStream bos = TestUtil.setOutToByteArray();
        app.run();
        TestUtil.clearSetOutToByteArray();

        // then
        List<Wise> wises = repository.findAll();
        assertThat(wises).hasSize(3);
        assertThat(bos.toString())
                .contains("이순신")
                .contains("나의 죽음을 적들에게 알리지 말라!")
                .contains("도로스 버나드")
                .contains("용기는 기도를 마친 두려움이다.")
                .contains("토마스 제퍼슨")
                .contains("돈이 수중에 들어오기 전까진 절대로 쓰지 마라.");
    }

    @Test
    @DisplayName("삭제")
    void removeTest() {
        //given
        Scanner scanner = TestUtil.getScanner(
                """
                        등록
                        나의 죽음을 적들에게 알리지 말라!
                        이순신
                        등록
                        용기는 기도를 마친 두려움이다.
                        도로스 버나드
                        삭제?id=1
                        목록
                        종료
                        """
        );
        TestConfig config = new TestConfig(scanner);
        WiseRepository repository = config.wiseRepository();
        App app = initApp(config, repository, scanner);

        // when
        ByteArrayOutputStream bos = TestUtil.setOutToByteArray();
        app.run();
        TestUtil.clearSetOutToByteArray();

        // then
        List<Wise> wises = repository.findAll();
        assertThat(wises).hasSize(1);
        assertThat(bos.toString())
                .contains("도로스 버나드")
                .contains("용기는 기도를 마친 두려움이다.")
                .contains("번 명언이 삭제되었습니다.");
    }

    @Test
    @DisplayName("수정")
    void modifyTest() {
        //given
        Scanner scanner = TestUtil.getScanner(
                """
                        등록
                        나의 죽음을 적들에게 알리지 말라!
                        이순신
                        수정?id=1
                        성공의 겉모습 만큼 성공하는 것은 없다.
                        크리스토퍼 래쉬
                        목록
                        종료
                        """
        );
        TestConfig config = new TestConfig(scanner);
        WiseRepository repository = config.wiseRepository();
        App app = initApp(config, repository, scanner);

        // when
        ByteArrayOutputStream bos = TestUtil.setOutToByteArray();
        app.run();
        TestUtil.clearSetOutToByteArray();

        // then
        List<Wise> wises = repository.findAll();
        assertThat(wises).hasSize(1);
        Wise wise = wises.getFirst();
        assertThat(wise.getContent()).isEqualTo("성공의 겉모습 만큼 성공하는 것은 없다.");
        assertThat(wise.getAuthor()).isEqualTo("크리스토퍼 래쉬");
        assertThat(bos.toString())
                .contains("번 명언이 수정되었습니다.");
    }

    private static App initApp(TestConfig config, WiseRepository repository, Scanner scanner) {
        return App.testApp(
                config.wiseController(repository),
                config.systemController(),
                repository,
                scanner
        );
    }
}
