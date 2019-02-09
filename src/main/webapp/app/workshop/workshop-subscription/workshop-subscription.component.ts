import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { IUser, User } from 'app/core';
import { Workshop } from 'app/shared/model/workshop.model';
import { IWorkshopDate, WorkshopDate } from 'app/shared/model/workshop-date.model';
import { WorkshopSubscriptionService } from 'app/entities/workshop-subscription';
import { SubscriptionState, WorkshopSubscription } from 'app/shared/model/workshop-subscription.model';
import { MatDialog, MatDialogRef } from '@angular/material';
import { WorkshopService } from 'app/entities/workshop';
import { ProgressSpinnerDialogComponent } from 'app/shared/dialog/progress-spinner-dialog.component';
import { ConfirmationDialogComponent } from 'app/shared/dialog/confirmation-dialog.component';

@Component({
    selector: 'jhi-workshop-subscription',
    templateUrl: './workshop-subscription.component.html',
    styleUrls: ['workshop-subscription.css']
})
export class WorkshopSubscriptionComponent implements OnInit {
    user: IUser = new User();
    workshop: Workshop = new Workshop();
    workshopDate: WorkshopDate = new WorkshopDate();

    constructor(
        private route: ActivatedRoute,
        private workshopService: WorkshopService,
        private workshopSubscriptionService: WorkshopSubscriptionService,
        private dialog: MatDialog
    ) {
        if (this.route.snapshot.data.workshop) {
            this.workshop = this.route.snapshot.data.workshop;
        } else {
            this.workshopService.find(this.route.snapshot.params['id'], true).subscribe(workshop => {
                this.workshop = workshop.body;
            });
        }

        if (this.route.snapshot.params['date']) {
            this.load(parseInt(this.route.snapshot.params['date'], 10));
        }
    }

    ngOnInit(): void {}

    submitForm() {
        const subscription = new WorkshopSubscription();
        subscription.state = SubscriptionState.NEW;
        subscription.workshopId = this.workshopDate.id;
        subscription.user = this.user;

        const dialogRef: MatDialogRef<ProgressSpinnerDialogComponent> = this.dialog.open(ProgressSpinnerDialogComponent, {
            panelClass: 'transparent',
            disableClose: true
        });
        this.workshopSubscriptionService.create(subscription).subscribe(
            () => {
                dialogRef.close();
                this.dialog.open(ConfirmationDialogComponent, {
                    width: '250px',
                    data: {
                        title: 'Joepie',
                        message: `Inschrijving gelukt! Er is een e-mail verstuurd naar ${this.user.email} met verdere instructies.`
                    }
                });
            },
            error => {
                dialogRef.close();
                this.dialog.open(ConfirmationDialogComponent, {
                    width: '250px',
                    data: {
                        title: 'Oeps...',
                        message: 'Er ging iets, probeer later opnieuw of contacteer annemie.rousseau@telenet.be.'
                    }
                });
            }
        );
    }

    load(id: number) {
        this.workshopDate = this.workshop.dates.find(date => {
            return date.id === id;
        });
    }

    chooseDate(pickedDate: IWorkshopDate) {
        this.workshopDate = this.workshop.dates.find(date => {
            return pickedDate === date;
        });
    }
}
