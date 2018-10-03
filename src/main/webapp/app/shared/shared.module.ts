import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { NgbDateAdapter } from '@ng-bootstrap/ng-bootstrap';

import { NgbDateMomentAdapter } from './util/datepicker-adapter';
import { HasAnyAuthorityDirective, JhiLoginModalComponent, KranzenzoSharedCommonModule, KranzenzoSharedLibsModule } from './';
import { FileSelectDirective } from 'ng2-file-upload';

@NgModule({
    imports: [KranzenzoSharedLibsModule, KranzenzoSharedCommonModule],
    declarations: [JhiLoginModalComponent, HasAnyAuthorityDirective, FileSelectDirective],
    providers: [{ provide: NgbDateAdapter, useClass: NgbDateMomentAdapter }],
    entryComponents: [JhiLoginModalComponent],
    exports: [KranzenzoSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective, FileSelectDirective],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class KranzenzoSharedModule {}
