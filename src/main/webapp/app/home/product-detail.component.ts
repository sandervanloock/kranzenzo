import {Component, OnDestroy, OnInit} from '@angular/core';
import {Product, ProductService} from '../entities/product';
import {JhiAlertService, JhiEventManager} from 'ng-jhipster';
import {ActivatedRoute} from '@angular/router';
import {Subscription} from 'rxjs/Rx';
import {Image} from '../entities/image';

// import {C} from '@ng-bootstrap/ng-bootstrap/carousel'

@Component( {
                selector: 'jhi-product-detail', templateUrl: './product-detail.component.html', styleUrls: ['product-detail.css']
            } )
export class ProductDetailComponent implements OnInit, OnDestroy {

    product: Product;
    private subscription: Subscription;
    private eventSubscriber: Subscription;
    submittedAlert: any;
    images: string[] = [];

    constructor( private eventManager: JhiEventManager, private alertService: JhiAlertService, private productService: ProductService, private route: ActivatedRoute ) {
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
            this.product.images.forEach( ( img: Image ) => {
                const lastIndexOf = img.endpoint.lastIndexOf( '/' );
                this.images.push( ['http://kranzenzo-images.s3-website-eu-west-1.amazonaws.com/800x400', img.endpoint.slice( lastIndexOf, img.endpoint.length )].join( '' ) );
            } )
        } );
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy( this.eventSubscriber );
    }

    registerChangeInProducts() {
        this.eventSubscriber = this.eventManager.subscribe( 'productListModification', ( response ) => this.load( this.product.id ) );
        this.eventManager.subscribe( 'productOrderCompleted', ( response ) => this.setSubmittedAlert( response.content ) );
    }

    private setSubmittedAlert( props ) {
        const alert = this.alertService.addAlert( {
                                                      type: props.type, msg: props.msg, params: {}, timeout: 5000, toast: true
                                                  }, [] );
        this.submittedAlert = alert;
        window.scrollTo( 0, 0 )
    }

}
