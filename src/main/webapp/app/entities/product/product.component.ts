import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Subscription} from 'rxjs/Rx';
import {JhiAlertService, JhiEventManager} from 'ng-jhipster';

import {Product} from './product.model';
import {ProductService} from './product.service';
import {Principal, ResponseWrapper} from '../../shared';

@Component( {
                selector: 'jhi-product', templateUrl: './product.component.html'
            } )
export class ProductComponent implements OnInit, OnDestroy {
    products: Product[];
    productsFiltered: Product[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private productService: ProductService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private activatedRoute: ActivatedRoute,
        private principal: Principal ) {
        this.currentSearch = activatedRoute.snapshot.params['search'] ? activatedRoute.snapshot.params['search'] : '';
    }

    loadAll() {
        if ( this.currentSearch ) {
            this.productsFiltered = this.products.filter( ( p: Product ) => {
                return (p.name != null && p.name.toLowerCase().indexOf( this.currentSearch.toLowerCase() ) !== -1) || (p.description != null && p.description.toLowerCase().indexOf(
                    this.currentSearch.toLowerCase() ) !== -1);
            } );
            return;
        }
        this.productService.query().subscribe( ( res: ResponseWrapper ) => {
            this.products = res.json;
            this.productsFiltered = res.json;
            this.currentSearch = '';
        }, ( res: ResponseWrapper ) => this.onError( res.json ) );
    }

    search( query ) {
        if ( !query ) {
            return this.clear();
        }
        this.currentSearch = query;
        this.loadAll();
    }

    clear() {
        this.currentSearch = '';
        this.loadAll();
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then( ( account ) => {
            this.currentAccount = account;
        } );
        this.registerChangeInProducts();
    }

    ngOnDestroy() {
        this.eventManager.destroy( this.eventSubscriber );
    }

    trackId( index: number, item: Product ) {
        return item.id;
    }

    registerChangeInProducts() {
        this.eventSubscriber = this.eventManager.subscribe( 'productListModification', ( response ) => this.loadAll() );
    }

    private onError( error ) {
        this.jhiAlertService.error( error.message, null, null );
    }
}
