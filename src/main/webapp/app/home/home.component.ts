import {Component, OnInit} from '@angular/core';

import {ResponseWrapper} from '../shared';
import {Product, ProductService} from '../entities/product';

@Component( {
                selector: 'jhi-home', templateUrl: './home.component.html', styleUrls: ['home.css']

            } )
export class HomeComponent implements OnInit {

    items: Product[] = [];

    constructor( private productService: ProductService ) {
    }

    ngOnInit() {
        this.productService.query()
            .subscribe( ( data: ResponseWrapper ) => this.items = data.json );
    }


}
