package site.lets_onion.lets_onionApp.service.member;

public enum Redirection {
    LOCAL("http://localhost:8080/member/oauth/kakao/callback"),
    SERVER("https://api.lets-onion.site/member/oauth/kakao/callback");

    private String redirectUri;

    Redirection(final String redirectUri) {
        this.redirectUri = redirectUri;
    }

    public String getRedirectUri() {
        return redirectUri;
    }
}
