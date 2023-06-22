package bside.NotToDoClub.domain_name.auth.config.oauth.info;

public interface OAuth2UserInfo {
    String getProviderId();
    String getProvider();
    String getEmail();
    String getName();
}
