function out = exp_h(lf, lk, v, u )
    st = 1E-5;
    if (abs(lf - 0.5) < 1E-6) && (abs(lk - 0.5) < 1E-6)
        out = add_op( v, u );
    elseif (abs(lf - 0.5) < 1E-6) && (lk < 0.5)
        e1 = @(x) log(lk*x+1)/(1-lk)-log(x+1);
        xmax = fzero(e1, [st 20]);
        a = log(xmax+1)/xmax;
        if ndims(u) == 1
            y = 0;
            for i = 1:length(v)
                y = y + v(i)*u(i);
            end;
        elseif  ndims(u) == 2
            n = size(u);
            y = zeros(n(1:length(n)-1));
            for i = 1:length(v)
                y(:) = y(:) + v(i)*u(:,i);
            end;            
        elseif  ndims(u) == 3
            n = size(u);
            y = zeros(n(1:length(n)-1));
            for i = 1:length(v)
                y(:,:) = y(:,:) + v(i).*u(:,:,i);
            end;            
        end
        out = (exp((xmax*a).*y)-1)./xmax;        
    elseif (abs(lf - 0.5) < 1E-6) && (lk > 0.5)
        e1 = @(x) log(1-lk*x)/(1-lk)-log(1-x);
        xmax = fzero(e1, [st 20]);
        a = -log(1-xmax)/xmax;
        if ndims(u) == 1
            y = 0;
            for i = 1:length(v)
                y = y + v(i)*u(i);
            end;
        elseif  ndims(u) == 2
            n = size(u);
            y = zeros(n(1:length(n)-1));
            for i = 1:length(v)
                y(:) = y(:) + v(i)*u(:,i);
            end;            
        elseif  ndims(u) == 3
            n = size(u);
            y = zeros(n(1:length(n)-1));
            for i = 1:length(v)
                y(:,:) = y(:,:) + v(i).*u(:,:,i);
            end;            
        end
        out = (1-exp((-xmax*a).*y))./xmax;        
    elseif (abs(lk - 0.5) < 1E-6) && (lf < 0.5)
        e1 = @(x) lf*x/(1-exp(-x*(1-lf))) - x/(1-exp(-x));        
        xmax = fzero(e1,[st, 10]);    
        C = xmax;
        b = xmax/(1-exp(-xmax)); 

        if ndims(u) == 1
            y = 0;
            for i = 1:length(v)
                y = y + v(i)*log(1-C.*u(i)./b);
            end        
        elseif  ndims(u) == 2
            [n,nn] = size(u);    
            y = zeros(n);
            for i = 1:length(v)
                y(:) = y(:) + v(i)*log(1-C.*u(:,i)./b);
            end        
        elseif ndims(u) == 3
            [n,nn, nnn] = size(u);
            y = zeros(n,nn);        
            for i = 1:length(v)
                y(:,:) = y(:,:) + v(i)*log(1-C.*u(:,:,i)./b);
            end        
        end
        y = -y./C;
        out = y;        
    elseif (abs(lk - 0.5) < 1E-6) && (lf > 0.5)
        e1 = @(x) lf*x/(1+exp(x*(1-lf))) - x/(1+exp(x));        
        xmax = fzero(e1,[st, 10]);    
        C = xmax;
        b = xmax/(1+exp(xmax)); 

        if ndims(u) == 1
            y = 0;
            for i = 1:length(v)
                y = y + v(i)*log(C.*u(i)./b-1);
            end        
        elseif  ndims(u) == 2
            [n,nn] = size(u);    
            y = zeros(n);
            for i = 1:length(v)
                y(:) = y(:) + v(i)*log(C.*u(:,i)./b-1);
            end        
        elseif ndims(u) == 3
            [n,nn, nnn] = size(u);
            y = zeros(n,nn);        
            for i = 1:length(v)
                y(:,:) = y(:,:) + v(i)*log(C.*u(:,:,i)./b-1);
            end        
        end
        y = y./C;
        out = y;                
    elseif (lf < 0.5) && (lk < 0.5)
        e1 = @(x) lf .* (exp(x)-1) - exp(x.*(1-lf)) + 1;
        xmax = fzero(e1, [st 20]);
        e2 = @(a) lk*(exp(a*xmax)-1)-exp(a*xmax*(1-lk))+1;
        a = fzero(e2, [st 20]);

        b = (exp(a*xmax)-1)/(exp(xmax)-1);    
        c = b*(exp(xmax)-1);        
        if ndims(u) == 1
            y = 1;
            for i = 1:length(u)
                y = y * (1+c*u(i)/b)^(a*v(i));         
            end 
            y = y -1;
            y = y /c;
        elseif  ndims(u) == 2
            n = size(u);
            y = ones(n(1:length(n)-1));
            for i = 1:length(v)
                y(:) = y(:) .* (1+c.*u(:,i)./b).^(a.*v(i));         
            end 
            y(:) = y(:) -1;
            y(:) = y(:) ./c;        
        elseif ndims(u) == 3
            n = size(u);
            y = ones(n(1:length(n)-1));
            for i = 1:length(v)
                y(:,:) = y(:,:) .* (1+c.*u(:,:,i)./b).^(a.*v(i));         
            end 
            y = y - 1;
            y = y ./c;        
        end 
        out = y;
    elseif (lk < 0.5) && (lf > 0.5)
        e1 = @(x) lf .* (1 - exp(-x)) + exp(-x.*(1-lf)) - 1;
        xmax = fzero(e1, [st 20]);
        e2 = @(a) lk*(exp(a*xmax)-1)-exp(a*(xmax*(1-lk)))+1;
        a = fzero(e2, [st 20]);
        b = (exp(a*xmax)-1)/(1-exp(-xmax));        
        c = exp(a*xmax)-1;

        if ndims(u) == 1
            y = 1;
            for i = 1:length(u)
                y = y * (1-c*u(i)/b)^(-a*v(i));         
            end 
            y = y - 1;
            y = y / c;
        elseif  ndims(u) == 2
            n = size(u);
            y = ones(n(1:length(n)-1));
            for i = 1:length(v)
                y(:) = y(:) .* (1-c.*u(:,i)./b).^(-a*v(i));         
            end 
            y(:) = y(:) - 1;
            y(:) = y(:) ./c;        
        elseif ndims(u) == 3
            n = size(u);
            y = ones(n(1:length(n)-1));
            for i = 1:length(v)
                y(:,:) = y(:,:) .* (1-c.*u(:,:,i)./b).^(-a*v(i));         
            end 
            y = y - 1;
            y = y ./c;        
        end 
        out = y;
    elseif (lk > 0.5) && (lf > 0.5)
        e1 = @(x) lf .* (1-exp(-x)) + exp(-x.*(1-lf)) - 1;
        xmax = fzero(e1, [st 20]);
        e2 = @(a) lk*(1-exp(-a*xmax)) + exp(-a*xmax*(1-lk)) - 1;
        a = fzero(e2, [st 20]);

        b = (1-exp(-a*xmax))/(1-exp(-xmax));    
        c = b*(1-exp(-xmax));        
        if ndims(u) == 1
            y = 1;
            for i = 1:length(u)
                y = y * (1-c*u(i)/b)^(a*v(i));         
            end 
            y = 1 - y;
            y = y /c;
        elseif  ndims(u) == 2
            n = size(u);
            y = ones(n(1:length(n)-1));
            for i = 1:length(v)
                y(:) = y(:) .* (1-c.*u(:,i)./b).^(a.*v(i));         
            end 
            y = 1 - y;
            y = y ./c;        
        elseif ndims(u) == 3
            n = size(u);
            y = ones(n(1:length(n)-1));
            for i = 1:length(v)
                y(:,:) = y(:,:) .* (1-c.*u(:,:,i)./b).^(a.*v(i));         
            end 
            y = 1 - y;
            y = y ./c;        
        end 
        out = y;
    elseif (lk > 0.5) && (lf < 0.5)
        e1 = @(x) lf .* (exp(x)-1) - exp(x.*(1-lf)) + 1;
        xmax = fzero(e1, [st 20]);
        e2 = @(a) lk*(1-exp(-a*xmax))+exp(-a*xmax*(1-lk))-1;
        a = fzero(e2, [st 20]);
        b = (1-exp(-a*xmax))/(exp(-xmax)-1);        
        c = 1 - exp(-a*xmax);

        if ndims(u) == 1
            y = 1;
            for i = 1:length(u)
                y = y * (1+c*u(i)/b)^(-a*v(i));         
            end 
            y = 1 - y;
            y = y / c;
        elseif  ndims(u) == 2
            n = size(u);
            y = ones(n(1:length(n)-1));
            for i = 1:length(v)
                y(:) = y(:) .* (1+c.*u(:,i)./b).^(-a*v(i));         
            end 
            y = 1 - y;
            y(:) = y(:) ./c;        
        elseif ndims(u) == 3
            n = size(u);
            y = ones(n(1:length(n)-1));
            for i = 1:length(v)
                y(:,:) = y(:,:) .* (1+c.*u(:,:,i)./b).^(-a*v(i));         
            end 
            y = 1 - y;
            y = y ./c;        
        end 
        out = y;
        
    end        
end
