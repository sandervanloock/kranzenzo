import {CUSTOM_ELEMENTS_SCHEMA, NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';

import {KransenzoSharedModule} from '../../shared';
import {
    ProductComponent,
    ProductDeleteDialogComponent,
    ProductDeletePopupComponent,
    ProductDetailComponent,
    ProductDialogComponent,
    ProductPopupComponent,
    productPopupRoute,
    ProductPopupService,
    productRoute,
    ProductService,
} from './';

const ENTITY_STATES = [...productRoute, ...productPopupRoute,];

@NgModule( {
               imports: [KransenzoSharedModule, RouterModule.forChild( ENTITY_STATES )],
               declarations: [ProductComponent, ProductDetailComponent, ProductDialogComponent, ProductDeleteDialogComponent, ProductPopupComponent, ProductDeletePopupComponent,],
               entryComponents: [ProductComponent, ProductDialogComponent, ProductPopupComponent, ProductDeleteDialogComponent, ProductDeletePopupComponent,],
               providers: [ProductService, ProductPopupService,],
               schemas: [CUSTOM_ELEMENTS_SCHEMA]
           } )
export class KransenzoProductModule {
}
