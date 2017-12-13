import {Component, OnInit} from '@angular/core';
import {JhiEventManager} from 'ng-jhipster';
import {LoginModalService, Principal} from '../shared';
import {NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {Product, ProductService} from '../entities/product';
import {Subscription} from 'rxjs/Rx';
import {ActivatedRoute} from '@angular/router';

/*
Based on https://codepen.io/bryceyork/pen/MyPjPE

Inspired by https://www.google.com/design/spec/components/steppers.html#steppers-types-of-steppers

And leveraging the Creative Tim Material Bootstrap Library - http://demos.creative-tim.com/material-kit/index.html
 */
@Component( {
                selector: 'jhi-product-order', templateUrl: './product-order.component.html', styleUrls: ['stepper.css']
            } )
export class ProductOrderComponent implements OnInit {
    step: number;
    deliveryType: string;
    led: boolean;
    price: number;
    account: Account;
    modalRef: NgbModalRef;
    product: Product;

    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private loginModalService: LoginModalService,
        private principal: Principal,
        private eventManager: JhiEventManager,
        private productService: ProductService,
        private route: ActivatedRoute, ) {
    }

    ngOnInit() {
        this.step = 1;
        this.price = 50;
        this.deliveryType = 'pickup';

        this.principal.identity().then( ( account ) => {
            this.account = account;
        } );
        this.registerAuthenticationSuccess();

        this.subscription = this.route.params.subscribe( ( params ) => {
            this.load( params['id'] );
        } );
        this.registerChangeInProducts();
    }

    onSelectionChange( type: string ) {
        if ( this.deliveryType === 'pickup' && type === 'delivered' ) {
            this.price += 5;
        } else if ( this.deliveryType === 'delivered' && type === 'pickup' ) {
            this.price -= 5;
        }
        this.deliveryType = type;
    }

    toggleLed() {
        this.led = !this.led;
        if ( this.led ) {
            this.price += 0.5;
        } else {
            this.price -= 0.5;
        }
    }

    registerAuthenticationSuccess() {
        this.eventManager.subscribe( 'authenticationSuccess', ( message ) => {
            this.principal.identity().then( ( account ) => {
                this.account = account;
            } );
        } );
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
