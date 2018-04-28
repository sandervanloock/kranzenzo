import {Component, OnDestroy, OnInit} from '@angular/core';
import {JhiEventManager} from 'ng-jhipster';
import {LoginModalService, Principal, User, UserService} from '../../shared';
import {NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {Product, ProductPopupService, ProductService} from '../../entities/product';
import {ActivatedRoute} from '@angular/router';
import {Customer, CustomerService} from '../../entities/customer';
import {Http} from '@angular/http';
import {Order, OrderService} from '../../entities/order';
import {Observable} from 'rxjs/Observable';
import {PRICE_BATTERIES_INCLUDED, VAT_NUMBER} from '../../app.constants';

declare var google: any;

/*
Based on https://codepen.io/bryceyork/pen/MyPjPE

Inspired by https://www.google.com/design/spec/components/steppers.html#steppers-types-of-steppers

And leveraging the Creative Tim Material Bootstrap Library - http://demos.creative-tim.com/material-kit/index.html
 */
@Component( {
                selector: 'jhi-product-order', templateUrl: './product-order.component.html', styleUrls: ['product-order.css']
            } )
export class ProductOrderComponent implements OnInit {
    step: number;
    modalRef: NgbModalRef;
    vatNumber: string;

    product: Product = new Product();
    customer: Customer = new Customer();
    order: Order = new Order();

    constructor(
        private loginModalService: LoginModalService,
        private principal: Principal,
        private eventManager: JhiEventManager,
        private productService: ProductService, public productPopupService: ProductPopupService,
        private route: ActivatedRoute,
        private http: Http,
        private userService: UserService,
        private customerService: CustomerService,
        private orderService: OrderService ) {
    }

    ngOnInit() {
        this.step = 1;
        this.vatNumber = VAT_NUMBER;
        this.order.productId = this.product.id;
        this.customer.user = new User();

        this.principal.identity().then( ( account ) => {
            if ( account ) {
                this.customer.user = account;
            } else {
                this.customer.user = new User();
                this.customer.user.langKey = 'nl';
            }
        } );
        this.registerAuthenticationSuccess();

    }

    toggleLed() {
        this.order.includeBatteries = !this.order.includeBatteries;
    }

    getTotalPrice() {
        let price = this.product.price;
        if ( this.order.deliveryType === 0 && this.order.deliveryPrice ) {
            price += this.order.deliveryPrice;
        }
        if ( this.order.includeBatteries ) {
            price += PRICE_BATTERIES_INCLUDED * this.product.numberOfBatteries;
        }
        return Math.round( price * 100 ) / 100;
    }

    registerAuthenticationSuccess() {
        this.eventManager.subscribe( 'authenticationSuccess', ( message ) => {
            this.principal.identity().then( ( account ) => {
                this.customer.user = account;
            } );
        } );
    }

    submitForm() {
        this.updateCustomer().flatMap( ( customer ) => {
            this.customer = customer;
            this.order.customerId = customer.id;
            return this.orderService.create( this.order );
        } ).subscribe( ( order: Order ) => {
            this.handleSuccessfulOrder( order );
        }, ( error ) => {
            this.handleFailedOrder();
        } )
    }

    gotoStepTwo() {
        this.step = 2;
    }

    gotoStepTree() {
        this.step = 3;
    }

    private handleFailedOrder() {
        this.eventManager.broadcast( {
                                         name: 'productOrderCompleted', content: {type: 'error', msg: 'product.submitted.error',}
                                     } );
    }

    updateDeliveryPrice( price: number ) {
        this.order.deliveryPrice = price;
    }

    private handleSuccessfulOrder( order: Order ) {
        this.order = order;
        this.productPopupService.close();
        this.eventManager.broadcast( {
                                         name: 'productOrderCompleted', content: {type: 'success', msg: 'product.submitted.success',}
                                     } );
    }

    private updateCustomer(): Observable<Customer> {
        if ( this.customer.id === undefined ) {
            return this.customerService.create( this.customer );
        } else {
            return this.customerService.update( this.customer );
        }
    }
}

@Component( {
                selector: 'jhi-product-order-popup', template: ''
            } )
export class ProductOrderPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor( private route: ActivatedRoute, private productPopupService: ProductPopupService ) {
    }

    ngOnInit() {
        this.routeSub = this.route.params.subscribe( ( params ) => {
            this.productPopupService
                .open( ProductOrderComponent as Component, params['id'] );
        } );
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
