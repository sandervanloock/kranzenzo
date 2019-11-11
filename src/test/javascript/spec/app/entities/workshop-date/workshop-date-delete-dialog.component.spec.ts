import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { KranzenzoTestModule } from '../../../test.module';
import { WorkshopDateDeleteDialogComponent } from 'app/entities/workshop-date/workshop-date-delete-dialog.component';
import { WorkshopDateService } from 'app/entities/workshop-date/workshop-date.service';

describe('Component Tests', () => {
  describe('WorkshopDate Management Delete Component', () => {
    let comp: WorkshopDateDeleteDialogComponent;
    let fixture: ComponentFixture<WorkshopDateDeleteDialogComponent>;
    let service: WorkshopDateService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [KranzenzoTestModule],
        declarations: [WorkshopDateDeleteDialogComponent]
      })
        .overrideTemplate(WorkshopDateDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(WorkshopDateDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(WorkshopDateService);
      mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
      mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
        })
      ));
    });
  });
});
