import {Component, Input, OnInit} from '@angular/core';
import {Product} from '../entities/product';

@Component( {
                selector: 'jhi-home-product', templateUrl: './home-product.component.html', styles: []
            } )
export class HomeProductComponent implements OnInit {
    @Input() item: Product;

    constructor() {
    }

    ngOnInit() {
    }

}