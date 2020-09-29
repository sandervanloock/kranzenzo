import {ChangeDetectorRef, Component, OnInit, ViewChild} from '@angular/core';
import {JhiEventManager} from 'ng-jhipster';
import {ActivatedRoute} from '@angular/router';
import {HttpClient, HttpResponse} from '@angular/common/http';
import {MatDialog, MatDialogRef} from '@angular/material';
import {Observable} from 'rxjs/Observable';
import {ORDER_DELIVERY_ORIGIN, PRICE_BATTERIES_INCLUDED, PRICE_PER_KILOMETER_PER_KM, VAT_NUMBER} from '../../app.constants';
import {IProduct} from 'app/shared/model/product.model';
import {Customer, ICustomer} from 'app/shared/model/customer.model';
import {LoginModalService, Principal, UserService} from 'app/core';
import {DeliveryType, IProductOrder, ProductOrder} from 'app/shared/model/product-order.model';
import {ProductService} from 'app/entities/product';
import {CustomerService} from 'app/entities/customer';
import {ProductOrderService} from 'app/entities/product-order';
import {ProgressSpinnerDialogComponent} from 'app/shared/dialog/progress-spinner-dialog.component';
import {ConfirmationDialogComponent} from 'app/shared/dialog/confirmation-dialog.component';
import {PickupDateComponent} from 'app/shop/order/pickup-date.component';

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
    step = 1;
    vatNumber: string = VAT_NUMBER;

    product: IProduct;
    customer: ICustomer = new Customer();
    order: IProductOrder = new ProductOrder();

    @ViewChild( PickupDateComponent ) pickupDate;

    constructor(
        private loginModalService: LoginModalService,
        private principal: Principal,
        private eventManager: JhiEventManager,
        private productService: ProductService,
        private changeDetectorRef: ChangeDetectorRef,
        private route: ActivatedRoute,
        private http: HttpClient,
        private userService: UserService,
        private customerService: CustomerService,
        private orderService: ProductOrderService,
        private dialog: MatDialog
    ) {
        this.product = route.snapshot.data.product;
    }

    ngOnInit(): void {}

    toggleLed() {
        this.order.includeBatteries = !this.order.includeBatteries;
    }

    getTotalPrice() {
        let price = this.product.price;
        if (this.product.discount) {
            price -= this.product.discountAmount;
        }
        if (this.order.deliveryType === DeliveryType.DELIVERED && this.order.deliveryPrice) {
            price += this.order.deliveryPrice;
        }
        if (this.order.includeBatteries) {
            price += PRICE_BATTERIES_INCLUDED * this.product.numberOfBatteries;
        }
        return Math.round(price * 100) / 100;
    }

    registerAuthenticationSuccess() {
        this.eventManager.subscribe('authenticationSuccess', message => {
            this.principal.identity().then(account => {
                this.customer.user = account;
            });
        });
    }

    submitForm() {
        this.order.productId = this.product.id;
        if ( this.order.deliveryType === DeliveryType.PICKUP ) {
            const generatedDescriptionForPickupDate = `\nVerwachte ophaal datum op ${this.pickupDate.getSelectedDate().format( 'DD/MM/YYYY HH:mm' )}`;
            this.order.description = this.order.description + generatedDescriptionForPickupDate;
        }
        const dialogRef: MatDialogRef<ProgressSpinnerDialogComponent> = this.dialog.open( ProgressSpinnerDialogComponent, {
            panelClass: 'transparent', disableClose: true
        } );
        this.updateCustomer()
            .flatMap( customer => {
                this.customer = customer.body;
                this.customer.user.confirmEmail = this.customer.user.email;
                this.order.customerId = this.customer.id;
                if ( this.order.id ) {
                    return this.orderService.update( this.order );
                } else {
                    return this.orderService.create( this.order );
                }
            } )
            .subscribe(
                (order: HttpResponse<IProductOrder>) => {
                    dialogRef.close();
                    this.handleSuccessfulOrder(order.body);
                },
                error => {
                    dialogRef.close();
                    this.handleFailedOrder(error);
                }
            );
    }

    private handleFailedOrder(error) {
        console.error(error);
        this.dialog.open(ConfirmationDialogComponent, {
            width: '250px',
            data: {
                title: 'Oeps...',
                message: 'Er ging iets, probeer later opnieuw of contacteer annemie.rousseau@telenet.be.'
            }
        });
        this.eventManager.broadcast({
            name: 'productOrderCompleted',
            content: { type: 'error', msg: 'product.submitted.error' }
        });
    }

    updateDeliveryPrice() {
        if (this.customer.latitude && this.customer.longitude) {
            const directionsService = new google.maps.DirectionsService();
            directionsService.route(
                {
                    origin: ORDER_DELIVERY_ORIGIN,
                    destination: { lat: this.customer.latitude, lng: this.customer.longitude },
                    waypoints: [],
                    optimizeWaypoints: true,
                    travelMode: 'DRIVING'
                },
                (response, status) => {
                    if (status === 'OK') {
                        if (response.routes.length && response.routes[0].legs.length) {
                            const distance = response.routes[0].legs[0].distance.value;
                            const distanceWithDiscount = Math.max(0, distance - 10000);
                            const price = Math.round(distanceWithDiscount / 1000 * PRICE_PER_KILOMETER_PER_KM * 100) / 100;
                            console.log('Delivery price for distance ' + distance + ' is ' + price);
                            this.order.deliveryPrice = price;
                            this.changeDetectorRef.detectChanges();
                        }
                    } else {
                        console.error('Directions request failed due to ' + status);
                    }
                }
            );
        }
    }

    private handleSuccessfulOrder(order: IProductOrder) {
        this.dialog.open( ConfirmationDialogComponent, {
            width: '250px', data: {
                title: 'Joepie', message: `Bestelling gelukt! Er is een e-mail verstuurd naar ${order.customer.user.email} met de bevestiging.`
            }
        } );
        this.order = order;
        this.eventManager.broadcast( {
                                         name: 'productOrderCompleted', content: {type: 'success', msg: 'product.submitted.success'}
                                     } );
    }

    private updateCustomer(): Observable<HttpResponse<ICustomer>> {
        if ( this.customer.id === undefined ) {
            return this.customerService.create( this.customer );
        } else {
            return this.customerService.update( this.customer );
        }
    }
}
