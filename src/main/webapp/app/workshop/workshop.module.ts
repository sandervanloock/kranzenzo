import {NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';
import {SHOP_ROUTE} from './workshop.route';
import {WorkshopDetailComponent} from './workshop-detail/workshop-detail.component';
import {KransenzoSharedModule} from '../shared';
import {WorkshopSubscriptionComponent, WorkshopSubscriptionPopupComponent} from './workshop-subscription/workshop-subscription.component';
import {FloorPipe} from './workshop-detail/FloorPipe';
import {OrderModule} from '../shop/order/order.module';

@NgModule( {
               imports: [KransenzoSharedModule, RouterModule.forChild( SHOP_ROUTE ), OrderModule],
               declarations: [WorkshopDetailComponent, WorkshopSubscriptionComponent, WorkshopSubscriptionPopupComponent, FloorPipe],
               entryComponents: [WorkshopSubscriptionComponent]
           } )
export class WorkshopModule {
}
