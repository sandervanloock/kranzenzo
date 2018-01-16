import {CUSTOM_ELEMENTS_SCHEMA, NgModule} from '@angular/core';

import {KransenzoProductModule} from './product/product.module';
import {KransenzoImageModule} from './image/image.module';
import {KransenzoTagModule} from './tag/tag.module';
import {KransenzoCustomerModule} from './customer/customer.module';
import {KransenzoOrderModule} from './order/order.module';
import {KransenzoLocationModule} from './location/location.module';

/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule( {
               imports: [KransenzoProductModule, KransenzoImageModule, KransenzoTagModule, KransenzoCustomerModule, KransenzoOrderModule, KransenzoLocationModule,
                   /* jhipster-needle-add-entity-module - JHipster will add entity modules here */],
               declarations: [],
               entryComponents: [],
               providers: [],
               schemas: [CUSTOM_ELEMENTS_SCHEMA]
           } )
export class KransenzoEntityModule {
}
