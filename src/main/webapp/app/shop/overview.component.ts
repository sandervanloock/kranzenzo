import {Component, OnInit} from '@angular/core';
import {Principal, ResponseWrapper} from '../shared';
import {Product, ProductService} from '../entities/product';
import {ActivatedRoute} from '@angular/router';

@Component( {
                selector: 'jhi-overview', templateUrl: './overview.component.html', styleUrls: ['overview.css']
            } )
export class OverviewComponent implements OnInit {

    tagName: string;
    items: Product[] = [];

    constructor( private principal: Principal, private productService: ProductService, private route: ActivatedRoute ) {
    }

    ngOnInit() {
        this.route.queryParams
            .subscribe( ( params ) => {
                if ( params.tag ) {
                    this.tagName = params.tag;
                }
                this.loadProducts();
            } );
    }

    loadProducts() {
        const req = {activeOnly: true};
        if ( this.tagName ) {
            req['tagName'] = this.tagName;
        }
        this.productService.query( req )
            .subscribe( ( data: ResponseWrapper ) => this.items = data.json );

    }

}
