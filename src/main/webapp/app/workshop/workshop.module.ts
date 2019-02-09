import { NgModule } from '@angular/core';
import { WorkshopDetailComponent } from './workshop-detail/workshop-detail.component';
import {
    SubscriptionConfirmationDialogComponent,
    WorkshopSubscriptionComponent
} from './workshop-subscription/workshop-subscription.component';
import { RouterModule } from '@angular/router';
import { KranzenzoSharedModule } from 'app/shared';
import { SHOP_ROUTE } from 'app/workshop/workshop.route';
import { FloorPipe } from 'app/shared/util/floor.pipe';
import { ProgressSpinnerDialogComponent } from 'app/workshop/workshop-subscription/progress-spinner-dialog.component';

@NgModule({
    imports: [KranzenzoSharedModule, RouterModule.forChild(SHOP_ROUTE)],
    declarations: [
        WorkshopDetailComponent,
        WorkshopSubscriptionComponent,
        SubscriptionConfirmationDialogComponent,
        ProgressSpinnerDialogComponent
    ],
    entryComponents: [WorkshopSubscriptionComponent, SubscriptionConfirmationDialogComponent, ProgressSpinnerDialogComponent],
    providers: [FloorPipe]
})
export class WorkshopModule {}
