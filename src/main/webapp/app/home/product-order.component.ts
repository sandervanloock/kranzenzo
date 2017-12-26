import {Component, OnInit} from '@angular/core';
import {JhiEventManager} from 'ng-jhipster';
import {LoginModalService, Principal, User, UserService} from '../shared';
import {NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {Product, ProductService} from '../entities/product';
import {ActivatedRoute} from '@angular/router';
import {Customer, CustomerService} from '../entities/customer';
import {Http} from '@angular/http';
import {Order, OrderService} from '../entities/order';
import {Observable} from 'rxjs/Observable';

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
    modalRef: NgbModalRef;
    price: number;
    deliveryPrice: 5; //TODO make this dynamic with google maps

    product: Product;
    user: User = new User();
    customer: Customer = new Customer();
    order: Order = new Order();

    constructor(
        private loginModalService: LoginModalService,
        private principal: Principal,
        private eventManager: JhiEventManager,
        private productService: ProductService,
        private route: ActivatedRoute,
        private http: Http,
        private userService: UserService,
        private customerService: CustomerService,
        private orderService: OrderService ) {
    }

    ngOnInit() {
        this.step = 1;

        this.principal.identity().then( ( account ) => {
            if ( account ) {
                this.user = account;
                this.customer.userId = account.id;
            }
        } );
        this.registerAuthenticationSuccess();

        this.route.params.subscribe( ( params ) => {
            this.loadProduct( params['id'] );
        } );
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
                this.user = account;
                this.customer.userId = account.id;
            } );
        } );
    }

    loadProduct( id ) {
        this.productService.find( id ).subscribe( ( product ) => {
            this.product = product;
            this.price = this.product.price;
            this.order.productId = product.id;
        } );
    }

    updateLocation( e ) {
        const locationString = `${this.customer.street},${this.customer.city}`;
        //TODO calculate deliveryPrice with google maps
        /*this.http.get(
            `https://maps.googleapis.com/maps/api/directions/json?origin=Zorgvliet,Sint-Katelijne-waver&destination=${locationString}&key=AIzaSyClcpip4cpRugakVB8zitzdjxfo6qRPDic` )
            .subscribe( ( data: any ) => {
                console.log( data )
            } );
            */
    }

    submitForm() {
        this.orderService.create( this.order ).subscribe( ( order: Order ) => {
            this.order = order;
        } )
    }

    createCustomer() {
        if ( this.customer.id === undefined ) {
            this.updateCustomer( this.customerService.create( this.customer ) );
        } else {
            this.updateCustomer( this.customerService.update( this.customer ) );
        }
    }

    private updateCustomer( observable: Observable<Customer> ) {
        observable.subscribe( ( res: Customer ) => {
            this.customer = res;
            this.order.customerId = res.id;
        } )
    }
}
