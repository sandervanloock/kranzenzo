import { Route } from '@angular/router';
import { WorkshopDetailComponent } from './workshop-detail/workshop-detail.component';
import { WorkshopResolve } from 'app/entities/workshop';
import { WorkshopSubscriptionComponent } from 'app/workshop/workshop-subscription/workshop-subscription.component';

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
        path: 'kranzenzo-workshop/:id/subscription',
        component: WorkshopSubscriptionComponent,
        data: {
            authorities: [],
            pageTitle: 'home.title'
        }
    },
    {
        path: 'kranzenzo-workshop/:id/subscription/:date',
        component: WorkshopSubscriptionComponent,
        resolve: {
            workshop: WorkshopResolve
        },
        data: {
            authorities: [],
            pageTitle: 'home.title'
        }
    }
];
