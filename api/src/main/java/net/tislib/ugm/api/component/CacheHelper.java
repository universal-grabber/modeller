package net.tislib.ugm.api.component;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.springframework.stereotype.Component;

@Component
public class CacheHelper {

    private final Cache<String, String> pageCache;

    public CacheHelper() {
        CacheManager cacheManager = CacheManagerBuilder
                .newCacheManagerBuilder().build();
        cacheManager.init();

        pageCache = cacheManager
                .createCache("pageCache", CacheConfigurationBuilder
                        .newCacheConfigurationBuilder(
                                String.class, String.class,
                                ResourcePoolsBuilder.heap(10)));
    }

    public Cache<String, String> getPageCache() {
        return pageCache;
    }

    // standard getters and setters
}
