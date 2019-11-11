package be.sandervl.kranzenzo.config;

import io.github.jhipster.config.JHipsterProperties;
import io.github.jhipster.config.jcache.BeanClassLoaderAwareJCacheRegionFactory;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.jsr107.Eh107Configuration;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration <Object, Object> jcacheConfiguration;

    public CacheConfiguration( JHipsterProperties jHipsterProperties ) {
        BeanClassLoaderAwareJCacheRegionFactory.setBeanClassLoader( this.getClass().getClassLoader() );
        JHipsterProperties.Cache.Ehcache ehcache =
            jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder( Object.class, Object.class,
                ResourcePoolsBuilder.heap( ehcache.getMaxEntries() ) )
                                     .withExpiry( ExpiryPolicyBuilder
                                         .timeToLiveExpiration( Duration.ofSeconds( ehcache.getTimeToLiveSeconds() ) ) )
                                     .build() );
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            cm.createCache( be.sandervl.kranzenzo.repository.UserRepository.USERS_BY_LOGIN_CACHE, jcacheConfiguration );
            cm.createCache( be.sandervl.kranzenzo.repository.UserRepository.USERS_BY_EMAIL_CACHE, jcacheConfiguration );
            cm.createCache( be.sandervl.kranzenzo.domain.User.class.getName(), jcacheConfiguration );
            cm.createCache( be.sandervl.kranzenzo.domain.Authority.class.getName(), jcacheConfiguration );
            cm.createCache( be.sandervl.kranzenzo.domain.User.class.getName() + ".authorities", jcacheConfiguration );
            cm.createCache( be.sandervl.kranzenzo.domain.Product.class.getName(), jcacheConfiguration );
            cm.createCache( be.sandervl.kranzenzo.domain.Product.class.getName() + ".images", jcacheConfiguration );
            cm.createCache( be.sandervl.kranzenzo.domain.Product.class.getName() + ".orders", jcacheConfiguration );
            cm.createCache( be.sandervl.kranzenzo.domain.Product.class.getName() + ".tags", jcacheConfiguration );
            cm.createCache( be.sandervl.kranzenzo.domain.Image.class.getName(), jcacheConfiguration );
            cm.createCache( be.sandervl.kranzenzo.domain.Tag.class.getName(), jcacheConfiguration );
            cm.createCache( be.sandervl.kranzenzo.domain.Tag.class.getName() + ".workshops", jcacheConfiguration );
            cm.createCache( be.sandervl.kranzenzo.domain.Tag.class.getName() + ".products", jcacheConfiguration );
            cm.createCache( be.sandervl.kranzenzo.domain.Customer.class.getName(), jcacheConfiguration );
            cm.createCache( be.sandervl.kranzenzo.domain.Customer.class.getName() + ".orders", jcacheConfiguration );
            cm.createCache( be.sandervl.kranzenzo.domain.ProductOrder.class.getName(), jcacheConfiguration );
            cm.createCache( be.sandervl.kranzenzo.domain.Location.class.getName(), jcacheConfiguration );
            cm.createCache( be.sandervl.kranzenzo.domain.Workshop.class.getName(), jcacheConfiguration );
            cm.createCache( be.sandervl.kranzenzo.domain.Workshop.class.getName() + ".dates", jcacheConfiguration );
            cm.createCache( be.sandervl.kranzenzo.domain.Workshop.class.getName() + ".images", jcacheConfiguration );
            cm.createCache( be.sandervl.kranzenzo.domain.Workshop.class.getName() + ".tags", jcacheConfiguration );
            cm.createCache( be.sandervl.kranzenzo.domain.WorkshopDate.class.getName(), jcacheConfiguration );
            cm.createCache( be.sandervl.kranzenzo.domain.WorkshopDate.class
                .getName() + ".subscriptions", jcacheConfiguration );
            cm.createCache( be.sandervl.kranzenzo.domain.WorkshopSubscription.class.getName(), jcacheConfiguration );
            cm.createCache( be.sandervl.kranzenzo.domain.HomepageSettings.class.getName(), jcacheConfiguration );
            // jhipster-needle-ehcache-add-entry
        };
    }
}
