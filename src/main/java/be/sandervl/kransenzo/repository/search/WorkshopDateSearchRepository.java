package be.sandervl.kransenzo.repository.search;

import be.sandervl.kransenzo.domain.WorkshopDate;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the WorkshopDate entity.
 */
public interface WorkshopDateSearchRepository extends ElasticsearchRepository <WorkshopDate, Long> {
}
