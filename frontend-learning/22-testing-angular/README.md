# Módulo 22: Testing en Angular

## 1. TestBed y ComponentFixture

```typescript
describe('UsuarioListComponent', () => {
  let component: UsuarioListComponent;
  let fixture: ComponentFixture<UsuarioListComponent>;
  let usuarioService: jasmine.SpyObj<UsuarioService>;

  beforeEach(async () => {
    const spy = jasmine.createSpyObj('UsuarioService', ['getAll', 'eliminar']);

    await TestBed.configureTestingModule({
      imports: [UsuarioListComponent],
      providers: [
        { provide: UsuarioService, useValue: spy },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(UsuarioListComponent);
    component = fixture.componentInstance;
    usuarioService = TestBed.inject(UsuarioService) as jasmine.SpyObj<UsuarioService>;
  });

  it('debe crear el componente', () => {
    expect(component).toBeTruthy();
  });

  it('debe cargar usuarios al inicializar', () => {
    const mockUsuarios = [{ id: 1, nombre: 'Ana' }, { id: 2, nombre: 'Luis' }];
    usuarioService.getAll.and.returnValue(of(mockUsuarios));

    fixture.detectChanges(); // dispara ngOnInit

    expect(component.usuarios()).toEqual(mockUsuarios);
  });

  it('debe mostrar la tabla cuando hay usuarios', () => {
    usuarioService.getAll.and.returnValue(of([{ id: 1, nombre: 'Ana' }]));
    fixture.detectChanges();

    const tabla = fixture.nativeElement.querySelector('table');
    expect(tabla).toBeTruthy();
  });
});
```

## 2. Testing de Servicios

```typescript
describe('ProductoService', () => {
  let service: ProductoService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        ProductoService,
        provideHttpClient(),
        provideHttpClientTesting(),
      ],
    });
    service = TestBed.inject(ProductoService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify(); // verifica que no hay requests pendientes
  });

  it('debe obtener productos', () => {
    const mockProductos = [{ id: 1, nombre: 'Laptop', precio: 999 }];

    service.getAll().subscribe(productos => {
      expect(productos).toEqual(mockProductos);
    });

    const req = httpMock.expectOne('/api/productos');
    expect(req.request.method).toBe('GET');
    req.flush(mockProductos);
  });

  it('debe crear un producto', () => {
    const nuevo = { nombre: 'Mouse', precio: 25 };

    service.crear(nuevo).subscribe(producto => {
      expect(producto.id).toBe(1);
    });

    const req = httpMock.expectOne('/api/productos');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(nuevo);
    req.flush({ id: 1, ...nuevo });
  });
});
```

## 3. Testing de Formularios

```typescript
describe('UsuarioFormComponent', () => {
  let component: UsuarioFormComponent;
  let fixture: ComponentFixture<UsuarioFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UsuarioFormComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(UsuarioFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('formulario debe ser inválido cuando está vacío', () => {
    expect(component.form.valid).toBeFalse();
  });

  it('formulario debe ser válido con datos correctos', () => {
    component.form.patchValue({
      nombre: 'Juan',
      email: 'juan@mail.com',
      edad: 25,
    });
    expect(component.form.valid).toBeTrue();
  });

  it('email debe mostrar error si es inválido', () => {
    const email = component.form.get('email');
    email?.setValue('no-es-email');
    expect(email?.hasError('email')).toBeTrue();
  });
});
```

## 4. Testing con Signals

```typescript
describe('CarritoStateService', () => {
  let service: CarritoStateService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CarritoStateService);
  });

  it('debe agregar un producto', () => {
    service.agregar({ id: 1, nombre: 'Laptop', precio: 999 });
    expect(service.carritoItems().length).toBe(1);
    expect(service.total()).toBe(999);
  });

  it('debe calcular total correctamente', () => {
    service.agregar({ id: 1, nombre: 'Laptop', precio: 999 });
    service.agregar({ id: 2, nombre: 'Mouse', precio: 25 });
    expect(service.total()).toBe(1024);
  });
});
```

## 5. Ejercicios

1. Escribe tests para un componente que muestra una lista (mock del servicio, verificar render).
2. Testea un servicio HTTP con HttpTestingController (GET, POST, manejo de errores).
3. Testea un formulario reactivo (validaciones, submit, estados).
4. Crea tests para un guard de autenticación (redirige si no hay token).
5. Testea un servicio de estado con signals (agregar, eliminar, computed values).

---

## Siguiente Módulo
→ [23-Performance](../23-performance/README.md)
