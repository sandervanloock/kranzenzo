package be.sandervl.kransenzo.repository.search;

import be.sandervl.kransenzo.domain.Product;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Product entity.
 */
public interface ProductSearchRepository extends ElasticsearchRepository <Product, Long> {
}
