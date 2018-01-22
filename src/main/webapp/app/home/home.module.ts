import {CUSTOM_ELEMENTS_SCHEMA, NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';

import {KransenzoSharedModule} from '../shared';

import {HOME_ROUTE, HomeComponent} from './';
import {HomeProductComponent} from './home-product.component';
import {ProductDetailComponent} from './product-detail.component';
import {ProductOrderComponent, ProductOrderPopupComponent} from './product-order.component';

import {CustomerAddressComponent} from './customer-address.component';

@NgModule({
              imports: [KransenzoSharedModule, RouterModule.forChild( HOME_ROUTE )],
              declarations: [HomeComponent, HomeProductComponent, ProductDetailComponent, ProductOrderComponent, ProductOrderPopupComponent, CustomerAddressComponent
    ],
              entryComponents: [ProductOrderComponent
    ],
    providers: [
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class KransenzoHomeModule {}
