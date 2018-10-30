import { NgModule } from '@angular/core';
import { WorkshopDetailComponent } from './workshop-detail/workshop-detail.component';
import { WorkshopSubscriptionComponent } from './workshop-subscription/workshop-subscription.component';
import { RouterModule } from '@angular/router';
import { KranzenzoSharedModule } from 'app/shared';
import { SHOP_ROUTE } from 'app/workshop/workshop.route';
import { FloorPipe } from 'app/shared/util/floor.pipe';

@NgModule({
    imports: [KranzenzoSharedModule, RouterModule.forChild(SHOP_ROUTE)],
    declarations: [WorkshopDetailComponent, WorkshopSubscriptionComponent],
    entryComponents: [WorkshopSubscriptionComponent],
    providers: [FloorPipe]
})
export class WorkshopModule {}
