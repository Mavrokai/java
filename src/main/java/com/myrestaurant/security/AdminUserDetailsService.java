package com.myrestaurant.security;

import com.myrestaurant.domain.RestaurantConfig;
import com.myrestaurant.service.ConfigService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Fournit l'unique compte administrateur, dont le mot de passe est defini
 * lors de la configuration initiale du site.
 */
@Service
public class AdminUserDetailsService implements UserDetailsService {

    public static final String ADMIN_USERNAME = "admin";

    private final ConfigService configService;

    public AdminUserDetailsService(ConfigService configService) {
        this.configService = configService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        RestaurantConfig config = configService.getConfig()
                .orElseThrow(() -> new UsernameNotFoundException("Site non configure"));

        if (!ADMIN_USERNAME.equalsIgnoreCase(username)) {
            throw new UsernameNotFoundException("Utilisateur inconnu");
        }

        return new User(
                ADMIN_USERNAME,
                config.getAdminPasswordHash(),
                List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
        );
    }
}
