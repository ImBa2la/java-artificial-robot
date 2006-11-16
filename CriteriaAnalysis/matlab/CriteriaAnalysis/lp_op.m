function out = lp_op(lf, lk, v, u )
    % lf > 0.5
    lk = 0.5;
    e1 = @eq1;    
    function y = eq1(x)
        y = lf.*x/(1-exp(-x.*(1-lf))) - x./(1-exp(-x));
    end
    
    xmax = fzero(e1,[eps, 10]);    
    C = xmax;
    b = xmax/(1-exp(-xmax)); 
    
    if ndims(u) == 1
        y = 0
        for i = 1:length(v)
            y = y + v(i)*log(1-C.*u(i)./b);
        end        
    elseif  ndims(u) == 2
        [n,nn] = size(u)     
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
end