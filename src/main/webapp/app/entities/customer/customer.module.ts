import {CUSTOM_ELEMENTS_SCHEMA, NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';

import {KransenzoSharedModule} from '../../shared';
import {
    CustomerComponent,
    CustomerDeleteDialogComponent,
    CustomerDeletePopupComponent,
    CustomerDetailComponent,
    CustomerDialogComponent,
    CustomerPopupComponent,
    customerPopupRoute,
    CustomerPopupService,
    customerRoute,
    CustomerService,
} from './';

const ENTITY_STATES = [...customerRoute, ...customerPopupRoute,];

@NgModule( {
               imports: [KransenzoSharedModule, RouterModule.forRoot( ENTITY_STATES, {useHash: true} )],
               declarations: [CustomerComponent, CustomerDetailComponent, CustomerDialogComponent, CustomerDeleteDialogComponent, CustomerPopupComponent,
                              CustomerDeletePopupComponent,],
               entryComponents: [CustomerComponent, CustomerDialogComponent, CustomerPopupComponent, CustomerDeleteDialogComponent, CustomerDeletePopupComponent,],
               providers: [CustomerService, CustomerPopupService,],
               schemas: [CUSTOM_ELEMENTS_SCHEMA]
           } )
export class KransenzoCustomerModule {
}
