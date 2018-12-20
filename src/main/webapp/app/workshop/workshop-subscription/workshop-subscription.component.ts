import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { IUser, User } from 'app/core';
import { Workshop } from 'app/shared/model/workshop.model';
import { IWorkshopDate, WorkshopDate } from 'app/shared/model/workshop-date.model';
import { WorkshopSubscriptionService } from 'app/entities/workshop-subscription';
import { SubscriptionState, WorkshopSubscription } from 'app/shared/model/workshop-subscription.model';
import { MatSnackBar } from '@angular/material';
import { WorkshopService } from 'app/entities/workshop';

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
        private snackBar: MatSnackBar
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

        this.workshopSubscriptionService.create(subscription).subscribe(
            () => {
                const snackBarRef = this.snackBar.open('Inschrijving gelukt, bekijk je email voor de bevestiging');
            },
            error => {
                const snackBarRef = this.snackBar.open('Er ging iets, probeer later opnieuw of contacteer annemie.rousseau@telenet.be');
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
