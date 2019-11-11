import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { KranzenzoTestModule } from '../../../test.module';
import { WorkshopSubscriptionComponent } from 'app/entities/workshop-subscription/workshop-subscription.component';
import { WorkshopSubscriptionService } from 'app/entities/workshop-subscription/workshop-subscription.service';
import { WorkshopSubscription } from 'app/shared/model/workshop-subscription.model';

describe('Component Tests', () => {
  describe('WorkshopSubscription Management Component', () => {
    let comp: WorkshopSubscriptionComponent;
    let fixture: ComponentFixture<WorkshopSubscriptionComponent>;
    let service: WorkshopSubscriptionService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [KranzenzoTestModule],
        declarations: [WorkshopSubscriptionComponent],
        providers: []
      })
        .overrideTemplate(WorkshopSubscriptionComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(WorkshopSubscriptionComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(WorkshopSubscriptionService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new WorkshopSubscription(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.workshopSubscriptions[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
