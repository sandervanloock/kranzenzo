import {Component, OnInit} from '@angular/core';
import {Product, ProductService} from '../entities/product';
import {JhiEventManager} from 'ng-jhipster';
import {ActivatedRoute} from '@angular/router';
import {Subscription} from 'rxjs/Rx';

// import {C} from '@ng-bootstrap/ng-bootstrap/carousel'

@Component( {
                selector: 'jhi-product-detail', templateUrl: './product-detail.component.html', styles: []
            } )
export class ProductDetailComponent implements OnInit {

    product: Product;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor( private eventManager: JhiEventManager, private productService: ProductService, private route: ActivatedRoute ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe( ( params ) => {
            this.load( params['id'] );
        } );
        this.registerChangeInProducts();

    }

    load( id ) {
        this.productService.find( id ).subscribe( ( product ) => {
            this.product = product;
        } );
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy( this.eventSubscriber );
    }

    registerChangeInProducts() {
        this.eventSubscriber = this.eventManager.subscribe( 'productListModification', ( response ) => this.load( this.product.id ) );
    }

}
