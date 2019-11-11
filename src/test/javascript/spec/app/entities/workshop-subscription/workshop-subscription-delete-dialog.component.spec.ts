import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { KranzenzoTestModule } from '../../../test.module';
import { WorkshopSubscriptionDeleteDialogComponent } from 'app/entities/workshop-subscription/workshop-subscription-delete-dialog.component';
import { WorkshopSubscriptionService } from 'app/entities/workshop-subscription/workshop-subscription.service';

describe('Component Tests', () => {
  describe('WorkshopSubscription Management Delete Component', () => {
    let comp: WorkshopSubscriptionDeleteDialogComponent;
    let fixture: ComponentFixture<WorkshopSubscriptionDeleteDialogComponent>;
    let service: WorkshopSubscriptionService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [KranzenzoTestModule],
        declarations: [WorkshopSubscriptionDeleteDialogComponent]
      })
        .overrideTemplate(WorkshopSubscriptionDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(WorkshopSubscriptionDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(WorkshopSubscriptionService);
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
