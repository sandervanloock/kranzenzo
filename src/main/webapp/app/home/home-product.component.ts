import {Component, Input, OnInit} from '@angular/core';

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

export class Product {
    constructor( public img?: string ) {
    }
}