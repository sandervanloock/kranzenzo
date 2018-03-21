import {Component, Input, OnInit} from '@angular/core';
import {Product} from '../entities/product';

@Component( {
                selector: 'jhi-home-product', templateUrl: './home-product.component.html', styleUrls: ['home.css']
            } )
export class HomeProductComponent implements OnInit {
    @Input() item: Product;

    imageUrl: string;

    constructor() {
    }

    ngOnInit() {
        if ( this.item.images.length ) {
            this.imageUrl = this.item.images[0].endpoint;
            const lastIndexOf = this.imageUrl.lastIndexOf( '/' );
            this.imageUrl = ['http://kranzenzo-images.s3-website-eu-west-1.amazonaws.com/800x400', this.imageUrl.slice( lastIndexOf, this.imageUrl.length )].join( '' );
        }
    }

}
