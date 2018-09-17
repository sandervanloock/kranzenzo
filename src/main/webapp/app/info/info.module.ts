import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { infoRoutes } from './info.route';
import { ContactComponent } from './contact.component';

import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { FaqComponent } from './faq.component';

@NgModule({
    imports: [CommonModule, RouterModule.forRoot(infoRoutes, { useHash: true }), NgbModule],
    declarations: [ContactComponent, FaqComponent]
})
export class InfoModule {}
