import {NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';
import {SHOP_ROUTE} from './workshop.route';
import {WorkshopDetailComponent} from './workshop-detail/workshop-detail.component';
import {KransenzoSharedModule} from '../shared';
import {WorkshopSubscriptionComponent, WorkshopSubscriptionPopupComponent} from './workshop-subscription/workshop-subscription.component';

@NgModule( {
               imports: [KransenzoSharedModule, RouterModule.forChild( SHOP_ROUTE ),],
               declarations: [WorkshopDetailComponent, WorkshopSubscriptionComponent, WorkshopSubscriptionPopupComponent],
               entryComponents: [WorkshopSubscriptionComponent]
           } )
export class WorkshopModule {
}
