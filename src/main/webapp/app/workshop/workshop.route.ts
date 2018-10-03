import { Route } from '@angular/router';
import { WorkshopDetailComponent } from './workshop-detail/workshop-detail.component';
import { WorkshopSubscriptionPopupComponent } from './workshop-subscription/workshop-subscription.component';
import { WorkshopResolve } from 'app/entities/workshop';

export const SHOP_ROUTE: Route[] = [
    {
        path: 'kranzenzo-workshop/:id',
        component: WorkshopDetailComponent,
        data: {
            authorities: [],
            pageTitle: 'home.title'
        }
    },
    {
        path: 'kranzenzo-workshop/:id/subscription/:date',
        component: WorkshopSubscriptionPopupComponent,
        resolve: {
            workshop: WorkshopResolve
        },
        data: {
            authorities: [],
            pageTitle: 'home.title'
        },
        outlet: 'popup'
    }
];
