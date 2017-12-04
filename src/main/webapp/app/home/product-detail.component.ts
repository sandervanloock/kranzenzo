import {Component, OnInit} from '@angular/core';
import {HomeProductComponent, Product} from './home-product.component';

// import {C} from '@ng-bootstrap/ng-bootstrap/carousel'

@Component( {
                selector: 'jhi-product-detail', templateUrl: './product-detail.component.html', styles: []
            } )
export class ProductDetailComponent implements OnInit {

    product: HomeProductComponent;

    constructor() {
    }

    ngOnInit() {
        this.product = new HomeProductComponent();
        this.product.item = new Product(
            ['http://via.placeholder.com/1800x900', 'http://via.placeholder.com/1800x900', 'http://via.placeholder.com/1800x900', 'http://via.placeholder.com/1800x900'] );
    }

}
