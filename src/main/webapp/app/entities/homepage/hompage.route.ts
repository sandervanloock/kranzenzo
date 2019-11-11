import { Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { HomepageComponent } from 'app/entities/homepage/homepage.component';

export const homepageSettingRoute: Routes = [
    {
        path: 'homepage-settings',
        component: HomepageComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Homepage Settings'
        },
        canActivate: [UserRouteAccessService]
    }
];
