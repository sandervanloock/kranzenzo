package be.sandervl.kransenzo.config;

import io.github.jhipster.config.JHipsterProperties;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expirations;
import org.ehcache.jsr107.Eh107Configuration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
@AutoConfigureAfter(value = {MetricsConfiguration.class})
@AutoConfigureBefore(value = {WebConfigurer.class, DatabaseConfiguration.class})
public class CacheConfiguration{

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties){
        JHipsterProperties.Cache.Ehcache ehcache =
            jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                                                                   ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                                     .withExpiry(Expirations.timeToLiveExpiration(
                                         Duration.of(ehcache.getTimeToLiveSeconds(), TimeUnit.SECONDS)))
                                     .build());
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer(){
        return cm -> {
            cm.createCache("users", jcacheConfiguration);
            cm.createCache(be.sandervl.kransenzo.domain.User.class.getName(), jcacheConfiguration);
            cm.createCache(be.sandervl.kransenzo.domain.Authority.class.getName(), jcacheConfiguration);
            cm.createCache(be.sandervl.kransenzo.domain.User.class.getName() + ".authorities", jcacheConfiguration);
            cm.createCache(be.sandervl.kransenzo.domain.SocialUserConnection.class.getName(), jcacheConfiguration);
            cm.createCache(be.sandervl.kransenzo.domain.Product.class.getName(), jcacheConfiguration);
            cm.createCache(be.sandervl.kransenzo.domain.Product.class.getName() + ".images", jcacheConfiguration);
            cm.createCache(be.sandervl.kransenzo.domain.Product.class.getName() + ".orders", jcacheConfiguration);
            cm.createCache(be.sandervl.kransenzo.domain.Product.class.getName() + ".tags", jcacheConfiguration);
            cm.createCache(be.sandervl.kransenzo.domain.Image.class.getName(), jcacheConfiguration);
            cm.createCache(be.sandervl.kransenzo.domain.Tag.class.getName(), jcacheConfiguration);
            cm.createCache(be.sandervl.kransenzo.domain.Tag.class.getName() + ".tags", jcacheConfiguration);
            cm.createCache( be.sandervl.kransenzo.domain.Tag.class.getName() + ".workshops", jcacheConfiguration );
            cm.createCache(be.sandervl.kransenzo.domain.Customer.class.getName(), jcacheConfiguration);
            cm.createCache(be.sandervl.kransenzo.domain.Customer.class.getName() + ".orders", jcacheConfiguration);
            cm.createCache(be.sandervl.kransenzo.domain.Order.class.getName(), jcacheConfiguration);
            cm.createCache(be.sandervl.kransenzo.domain.Location.class.getName(), jcacheConfiguration);
            cm.createCache( be.sandervl.kransenzo.domain.WorkshopSubscription.class.getName(), jcacheConfiguration );
            cm.createCache( be.sandervl.kransenzo.domain.WorkshopDate.class.getName(), jcacheConfiguration );
            cm.createCache( be.sandervl.kransenzo.domain.WorkshopDate.class
                .getName() + ".subscriptions", jcacheConfiguration );
            cm.createCache( be.sandervl.kransenzo.domain.Workshop.class.getName(), jcacheConfiguration );
            cm.createCache( be.sandervl.kransenzo.domain.Workshop.class.getName() + ".dates", jcacheConfiguration );
            cm.createCache( be.sandervl.kransenzo.domain.Workshop.class.getName() + ".images", jcacheConfiguration );
            cm.createCache( be.sandervl.kransenzo.domain.Workshop.class.getName() + ".tags", jcacheConfiguration );
            // jhipster-needle-ehcache-add-entry
        };
    }
}
