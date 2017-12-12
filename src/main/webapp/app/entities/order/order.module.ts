import {CUSTOM_ELEMENTS_SCHEMA, NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';

import {KransenzoSharedModule} from '../../shared';
import {
    OrderComponent,
    OrderDeleteDialogComponent,
    OrderDeletePopupComponent,
    OrderDetailComponent,
    OrderDialogComponent,
    OrderPopupComponent,
    orderPopupRoute,
    OrderPopupService,
    orderRoute,
    OrderService,
} from './';

const ENTITY_STATES = [...orderRoute, ...orderPopupRoute,];

@NgModule( {
               imports: [KransenzoSharedModule, RouterModule.forRoot( ENTITY_STATES, {useHash: true} )],
               declarations: [OrderComponent, OrderDetailComponent, OrderDialogComponent, OrderDeleteDialogComponent, OrderPopupComponent, OrderDeletePopupComponent,],
               entryComponents: [OrderComponent, OrderDialogComponent, OrderPopupComponent, OrderDeleteDialogComponent, OrderDeletePopupComponent,],
               providers: [OrderService, OrderPopupService,],
               schemas: [CUSTOM_ELEMENTS_SCHEMA]
           } )
export class KransenzoOrderModule {
}
