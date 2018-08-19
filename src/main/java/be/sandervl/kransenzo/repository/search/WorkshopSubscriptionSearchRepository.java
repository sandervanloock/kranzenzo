package be.sandervl.kransenzo.repository.search;

import be.sandervl.kransenzo.domain.WorkshopSubscription;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the WorkshopSubscription entity.
 */
public interface WorkshopSubscriptionSearchRepository extends ElasticsearchRepository <WorkshopSubscription, Long> {
}
