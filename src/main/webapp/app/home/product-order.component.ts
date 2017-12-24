import {Component, OnInit} from '@angular/core';
import {JhiEventManager} from 'ng-jhipster';
import {LoginModalService, Principal} from '../shared';
import {NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {Product, ProductService} from '../entities/product';
import {Subscription} from 'rxjs/Rx';
import {ActivatedRoute} from '@angular/router';
import {Customer} from '../entities/customer';
import {Http} from '@angular/http';
import {Order} from '../entities/order';

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
    account: Account;
    modalRef: NgbModalRef;
    price: number;
    deliveryPrice: number = 5; //TODO make this dynamic with google maps

    product: Product;
    customer: Customer = new Customer();
    order: Order = new Order();

    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private loginModalService: LoginModalService,
        private principal: Principal,
        private eventManager: JhiEventManager,
        private productService: ProductService, private route: ActivatedRoute, private http: Http ) {
    }

    ngOnInit() {
        this.step = 1;

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
        if ( type === 'DELIVERED' ) {
            this.price += this.deliveryPrice; //TODO calculate price for delivery
        } else if ( type === 'PICKUP' ) {
            this.price -= this.deliveryPrice;
        }
    }

    toggleLed() {
        this.order.includeBatteries = !this.order.includeBatteries;
        if ( this.order.includeBatteries ) {
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
            this.price = this.product.price;
        } );
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy( this.eventSubscriber );
    }

    registerChangeInProducts() {
        this.eventSubscriber = this.eventManager.subscribe( 'productListModification', ( response ) => this.load( this.product.id ) );
    }

    updateLocation( e ) {
        const locationString = `${this.customer.street},${this.customer.city}`;
        this.http.get(
            `https://maps.googleapis.com/maps/api/directions/json?origin=Zorgvliet,Sint-Katelijne-waver&destination=${locationString}&key=AIzaSyClcpip4cpRugakVB8zitzdjxfo6qRPDic` )
            .subscribe( ( data: any ) => {
                console.log( data )
            } );
    }

    submitForm() {
        //submit order with API here
    }
}
