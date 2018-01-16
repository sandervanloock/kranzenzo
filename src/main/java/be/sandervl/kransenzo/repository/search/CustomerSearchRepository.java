package be.sandervl.kransenzo.repository.search;

import be.sandervl.kransenzo.domain.Customer;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Customer entity.
 */
public interface CustomerSearchRepository extends ElasticsearchRepository <Customer, Long> {
}
