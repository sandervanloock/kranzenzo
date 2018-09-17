import { Routes } from '@angular/router';
import { ContactComponent } from './contact.component';
import { FaqComponent } from './faq.component';

export const infoRoutes: Routes = [
    {
        path: 'contact',
        data: { pageTitle: 'info.contact.title' },
        canActivate: [],
        component: ContactComponent
    },
    {
        path: 'faq',
        data: { pageTitle: 'info.faq.title' },
        canActivate: [],
        component: FaqComponent
    }
];
