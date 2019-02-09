import { NgModule } from '@angular/core';
import { WorkshopDetailComponent } from './workshop-detail/workshop-detail.component';
import { WorkshopSubscriptionComponent } from './workshop-subscription/workshop-subscription.component';
import { RouterModule } from '@angular/router';
import { KranzenzoSharedModule } from 'app/shared';
import { SHOP_ROUTE } from 'app/workshop/workshop.route';
import { FloorPipe } from 'app/shared/util/floor.pipe';
import { ConfirmationDialogComponent } from 'app/shared/dialog/confirmation-dialog.component';
import { ProgressSpinnerDialogComponent } from 'app/shared/dialog/progress-spinner-dialog.component';

@NgModule({
    imports: [KranzenzoSharedModule, RouterModule.forChild(SHOP_ROUTE)],
    declarations: [WorkshopDetailComponent, WorkshopSubscriptionComponent, ConfirmationDialogComponent, ProgressSpinnerDialogComponent],
    entryComponents: [WorkshopSubscriptionComponent, ConfirmationDialogComponent, ProgressSpinnerDialogComponent],
    providers: [FloorPipe]
})
export class WorkshopModule {}
