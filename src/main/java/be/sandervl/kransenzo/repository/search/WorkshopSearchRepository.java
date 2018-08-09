package be.sandervl.kransenzo.repository.search;

import be.sandervl.kransenzo.domain.Workshop;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Workshop entity.
 */
public interface WorkshopSearchRepository extends ElasticsearchRepository <Workshop, Long> {
}
