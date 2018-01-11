import {Component, OnInit} from '@angular/core';

import {Principal, ResponseWrapper} from '../shared';
import {Product, ProductService} from '../entities/product';
import {JhiEventManager} from 'ng-jhipster';

@Component( {
                selector: 'jhi-home', templateUrl: './home.component.html', styleUrls: ['home.css']

            } )
export class HomeComponent implements OnInit {
    account: Account;
    items: Product[] = [];

    constructor( private principal: Principal, private eventManager: JhiEventManager, private productService: ProductService ) {
    }

    ngOnInit() {
        this.principal.identity().then( ( account ) => {
            this.account = account;
        } );
        this.registerAuthenticationSuccess();

        this.productService.query( {activeOnly: true} )
            .subscribe( ( data: ResponseWrapper ) => this.items = data.json );
    }

    registerAuthenticationSuccess() {
        this.eventManager.subscribe( 'authenticationSuccess', ( message ) => {
            this.principal.identity().then( ( account ) => {
                this.account = account;
            } );
        } );
    }

}
