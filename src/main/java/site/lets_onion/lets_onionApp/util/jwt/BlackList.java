package site.lets_onion.lets_onionApp.util.jwt;

public interface BlackList {

    /* 블랙리스트에 토큰 삽입 */
    void putToken(String token, String date);

    /* 블랙리스트에 토큰이 있는지 확인*/
    boolean containsToken(String token);
}
