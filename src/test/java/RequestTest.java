import org.example.request.Request;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Scanner;

import static org.assertj.core.api.Assertions.assertThat;

public class RequestTest {

    @Test
    @DisplayName("종료")
    void exit() {
        //given
        Scanner scanner = TestUtil.getScanner("종료");

        //when
        Request request = new Request(scanner);

        //then
        assertThat(request.getPath()).isEqualTo("종료");
        assertThat(request.getQueryParams().isEmpty()).isTrue();
    }

    @Test
    @DisplayName("등록")
    void register() {
        //given
        Scanner scanner = TestUtil.getScanner("등록");

        //when
        Request request = new Request(scanner);

        //then
        assertThat(request.getPath()).isEqualTo("등록");
        assertThat(request.getQueryParams().isEmpty()).isTrue();
    }

    @Test
    @DisplayName("목록")
    void list() {
        //given
        Scanner scanner = TestUtil.getScanner("목록");

        //when
        Request request = new Request(scanner);

        //then
        assertThat(request.getPath()).isEqualTo("목록");
        assertThat(request.getQueryParams().isEmpty()).isTrue();
    }

    @Test
    @DisplayName("목록?page=1")
    void listWithPage() {
        //given
        Scanner scanner = TestUtil.getScanner("목록?page=1");

        //when
        Request request = new Request(scanner);

        //then
        assertThat(request.getPath()).isEqualTo("목록");
        assertThat(request.getQueryParams().size()).isEqualTo(1);
        assertThat(request.getQueryParams().get("page")).isEqualTo("1");
    }

    @Test
    @DisplayName("삭제?id=1")
    void remove() {
        //given
        Scanner scanner = TestUtil.getScanner("삭제?id=1");

        //when
        Request request = new Request(scanner);

        //then
        assertThat(request.getPath()).isEqualTo("삭제");
        assertThat(request.getQueryParams().size()).isEqualTo(1);
        assertThat(request.getQueryParams().get("id")).isEqualTo("1");
    }

    @Test
    @DisplayName("수정?id=1")
    void modify() {
        //given
        Scanner scanner = TestUtil.getScanner("수정?id=1");

        //when
        Request request = new Request(scanner);

        //then
        assertThat(request.getPath()).isEqualTo("수정");
        assertThat(request.getQueryParams().size()).isEqualTo(1);
        assertThat(request.getQueryParams().get("id")).isEqualTo("1");
    }

    @Test
    @DisplayName("빌드")
    void build() {
        //given
        Scanner scanner = TestUtil.getScanner("빌드");

        //when
        Request request = new Request(scanner);

        //then
        assertThat(request.getPath()).isEqualTo("빌드");
        assertThat(request.getQueryParams().isEmpty()).isTrue();
    }

    @Test
    @DisplayName("목록?keywordType=some&keyword=thing")
    void listWithKeyword() {
        //given
        Scanner scanner = TestUtil.getScanner("목록?keywordType=content&keyword=영광");

        //when
        Request request = new Request(scanner);

        //then
        assertThat(request.getPath()).isEqualTo("목록");
        assertThat(request.getQueryParams().size()).isEqualTo(2);
        assertThat(request.getQueryParams().get("keywordType")).isEqualTo("content");
        assertThat(request.getQueryParams().get("keyword")).isEqualTo("영광");
    }
}
