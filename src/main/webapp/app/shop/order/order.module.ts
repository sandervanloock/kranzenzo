import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {KranzenzoSharedCommonModule} from '../../shared/shared-common.module';
import {ConfirmationDialogComponent} from 'app/shared/dialog/confirmation-dialog.component';
import {ProgressSpinnerDialogComponent} from 'app/shared/dialog/progress-spinner-dialog.component';

@NgModule( {
               imports: [CommonModule, KranzenzoSharedCommonModule], entryComponents: [ConfirmationDialogComponent, ProgressSpinnerDialogComponent]
           } )
export class OrderModule {
}
