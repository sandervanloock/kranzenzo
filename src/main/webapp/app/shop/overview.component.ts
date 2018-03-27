import {Component, OnInit} from '@angular/core';
import {Principal, ResponseWrapper} from '../shared';
import {Product, ProductService} from '../entities/product';

@Component( {
                selector: 'jhi-overview', templateUrl: './overview.component.html', styleUrls: ['overview.css']
            } )
export class OverviewComponent implements OnInit {

    items: Product[] = [];

    constructor( private principal: Principal, private productService: ProductService ) {
    }

    ngOnInit() {
        this.productService.query( {activeOnly: true} )
            .subscribe( ( data: ResponseWrapper ) => this.items = data.json );
    }

    getImageUrl( item: Product ) {
        if ( item.images.length ) {
            const imageUrl = item.images[0].endpoint;
            const lastIndexOf = imageUrl.lastIndexOf( '/' );
            return ['http://kranzenzo-images.s3-website-eu-west-1.amazonaws.com/800x400', imageUrl.slice( lastIndexOf, imageUrl.length )]
                .join( '' );
        }
    }

}
